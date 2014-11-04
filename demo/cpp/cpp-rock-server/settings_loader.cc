
#include "settings_loader.h"

#include <iostream>

#include <thrift/protocol/TJSONProtocol.h>

#include <thrift/transport/TTransportUtils.h>
#include <thrift/transport/TFDTransport.h>

#include <unistd.h>
#include <fcntl.h>

using std::cout;
using std::endl;

using namespace boost;
using namespace apache::thrift;
using namespace apache::thrift::protocol;
using namespace apache::thrift::transport;

static const char * FILE_NAME = "/tmp/settings.json";

shared_ptr<rockserver::settings::Settings> loadSettings() {
  shared_ptr<rockserver::settings::Settings> result(new rockserver::settings::Settings());

  const char * fname = FILE_NAME;

  int fd = open(fname, O_RDONLY);
  if (fd < 0) {
    throw new std::logic_error(std::string("Unable to open file ") + fname);
  }

  shared_ptr<TTransport> transport(new TFDTransport(fd));
  shared_ptr<TProtocol> protocol(new TJSONProtocol(transport));

  result->read(protocol.get());

  return result;
}


void dumpSettings() {
  const char * fname = FILE_NAME;

  int fd = open(fname, O_CREAT | O_TRUNC | O_WRONLY, S_IRUSR | S_IWUSR | S_IXUSR);
  if (fd < 0) {
    throw new std::logic_error(std::string("Unable to open file ") + fname);
  }
  shared_ptr<TTransport> transport(new TFDTransport(fd));
  shared_ptr<TProtocol> protocol(new TJSONProtocol(transport));

  rockserver::settings::Settings value;
  value.__set_dbPath("/tmp/rocksdb-test");
  value.__set_portNumber(9011);

  value.write(protocol.get());

  transport->close();
  cout << "OK: dump to file=" << fname << " succeeded" << endl;
}

