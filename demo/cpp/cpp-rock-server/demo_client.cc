
#include "demo_client.h"

#include <iostream>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TCompactProtocol.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>

#include "thrift/gen-cpp/MetricsService.h"

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;

using ::std::cout;
using ::std::endl;
using ::boost::shared_ptr;
using ::rockserver::settings::Settings;
using ::rockserver::settings::ProtocolType;
using ::rockserver::settings::_ProtocolType_VALUES_TO_NAMES;
using ::metrics::MetricsServiceClient;
using ::metrics::MetricsEntry;
using ::metrics::MetricsEntryList;


//
// Helpers
//

/**
 * Duplicate of helper function from server.cc
 * This function is here to break dependency on server
 */
static inline shared_ptr<TProtocolFactory> getProtocolFactory(const shared_ptr<Settings>& settings) {
  switch (settings->protocolType) {
    case ProtocolType::type::BINARY:
      return shared_ptr<TProtocolFactory>(new TBinaryProtocolFactory());

    case ProtocolType::type::COMPACT:
      return shared_ptr<TProtocolFactory>(new TCompactProtocolFactory());
      
    case ProtocolType::type::DENSE:
      break; // unsupported

    case ProtocolType::type::ZLIB:
      break; // unsupported

    case ProtocolType::type::JSON:
      break; // unsupported
  }

  throw new std::logic_error(std::string("Unsupported protocolType=") + _ProtocolType_VALUES_TO_NAMES.at(settings->protocolType));
}


// entry point
void runDemoClient(const shared_ptr<Settings>& settings, ArgParser& ap) {
  // default properties
  const char * hostname = "localhost";
  
  // get property overrides
  while (true) {
    ap.next();
    if (ap.isArg("-host")) {
      hostname = ap.next();
    } else {
      break;
    }
  }

  cout << "Trying to connect to " << hostname << ':' << settings->portNumber << endl;

  shared_ptr<TTransport> socket(new TSocket("localhost", settings->portNumber));
  shared_ptr<TTransport> transport(new TBufferedTransport(socket));
  shared_ptr<TProtocol> protocol(getProtocolFactory(settings)->getProtocol(transport));

  MetricsServiceClient client(protocol);

  transport->open();

  if (ap.isArg("record")) {
    MetricsEntry entry;
    entry.__set_origin("TestClient");
    client.record(entry);
  } else if (ap.isArg("dumpAll")) {
    MetricsEntryList ml;
    client.getRecordedEntries(ml, 0, "");

    for (auto it = ml.entries.begin(); it != ml.entries.end(); ++it) {
      cout << "  Entry: metrics.origin=" << it->origin << endl;
    }
  } else {
    cout << "Unknown arg=" << ap.arg() << ", exiting";
    return;
  }

  cout << "OK." << endl;
}

