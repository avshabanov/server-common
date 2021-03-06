
#include <iostream>
#include <string>
#include <cstring>

#include "settings_loader.h"
#include "arg_parser.h"
#include "server.h"
#include "demo_client.h"

#include <rocksdb/db.h>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TCompactProtocol.h>

// Thrift Generated Sources
#include "thrift/gen-cpp/metrics_constants.h"
#include "thrift/gen-cpp/metrics_types.h"

using std::cerr;
using std::cout;
using std::endl;

using namespace boost;
using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;

//
// Constants
//

static const char * DEFAULT_SETTINGS_FILE_NAME = "settings.json";

//
// Helpers functions
//

static inline int usage(char * argv[], int code);

static inline void printSettings(const boost::shared_ptr<rockserver::settings::Settings>& settings) {
  cout << "{ \"dbPath\": \"" << settings->dbPath << "\", \"portNumber\": " << settings->portNumber <<
    ", \"protocolType\": \"" << rockserver::settings::_ProtocolType_VALUES_TO_NAMES.at(settings->protocolType) << '\"' <<
    " }" << endl;
}

static inline int parseArgs(int argc, char ** argv) {
  ArgParser ap(argc, argv);

  ap.next();
  if (ap.isArg(std::vector<const char *>({ "--help", "-h", "--usage" }))) {
    return usage(argv, 0);
  }

  const char * settingsFileName = DEFAULT_SETTINGS_FILE_NAME;

  if (ap.isArg("-sf", "--settings-file")) {
    settingsFileName = ap.next();
    ap.next();
  }

  if (ap.isArg("-is")) {
    dumpSettings(settingsFileName);
    cout << "Default settings saved to file" << endl;
    return 0;
  }

  if (ap.isArg("-ds")) {
    printSettings(loadSettings(settingsFileName));
    return 0;
  }

  if (ap.isArg("-s", "--start-server")) {
    auto settings = loadSettings(settingsFileName);
    cout << "Starting server. Settings=";
    printSettings(settings);
    rockserver::Server server(settings);
    server.run();
    return 0;
  }

  if (ap.isArg("-c", "--demo-client")) {
    auto settings = loadSettings(settingsFileName);
    cout << "Starting demo client. Settings=";
    printSettings(settings);
    runDemoClient(settings, ap);
    return 0;
  }

  throw new std::logic_error(std::string("Unknown command: ") + ap.arg());
}

static inline int usage(char * argv[], int code) {
  cout << "Usage: " << argv[0] << " ...arguments... Supported arguments:" << endl << 
    "     [-h, --help, --usage] Prints this message and exits" << endl <<
    "     [-is] Initializes source settings and exits" << endl << 
    "     [-ds] Dumps settings and exits" << endl <<
    "     [-sf, --settings-file] {PathToFile} Specifies path to settings file." << endl <<
    "                            Must be specified prior to -is and -ds arguments to be taken into an account." << endl <<
    "                            Default to " << DEFAULT_SETTINGS_FILE_NAME << endl <<
    endl;
  return code;
}

//
// Entry point
//

int main(int argc, char ** argv) {
  try {
    return parseArgs(argc, argv);
  } catch (std::exception * e) {
    // TODO: throwing and catching exceptions by pointer is never a good idea: who will clean up an allocated exception?
    // TODO: change it to the reference (and make sure Thrift behaves in a good way in this sense).
    cerr << "Error: " << e->what() << endl;
    return usage(argv, -1);
  }
}

