
#include "server.h"

#include <iostream>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TCompactProtocol.h>
#include <thrift/server/TSimpleServer.h>
#include <thrift/transport/TServerSocket.h>
#include <thrift/transport/TBufferTransports.h>

#include "thrift/gen-cpp/MetricsService.h"

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;
using namespace ::apache::thrift::server;

using ::std::cout;
using ::std::endl;
using ::boost::shared_ptr;
using ::rockserver::settings::Settings;
using ::rockserver::settings::ProtocolType;
using ::rockserver::settings::_ProtocolType_VALUES_TO_NAMES;
using ::metrics::MetricsServiceIf;
using ::metrics::MetricsServiceProcessor;
using ::metrics::MetricsEntry;
using ::metrics::MetricsEntryList;

namespace rockserver {

//
// Helpers
//

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

/**
 * Internal metrics service handler.
 * TODO: put into separate file.
 */
class MetricsServerHandler : public MetricsServiceIf {
public:
  void record(const MetricsEntry& entry) {
    cout << "recording entry" << endl;
  }

  void getRecordedEntries(MetricsEntryList& _return, const int32_t limit, const std::string& lastElementSeed) {
    cout << "print recorded entries" << endl;
  }
};

/**
 * Hidden implementation.
 */
class _ServerImpl {
private:
  shared_ptr<Settings> settings;

public:
  _ServerImpl(const shared_ptr<Settings>& settings) {
    this->settings = settings;
  }

  void run() {
    int port = settings->portNumber;
    shared_ptr<MetricsServerHandler> handler(new MetricsServerHandler());
    shared_ptr<TProcessor> processor(new MetricsServiceProcessor(handler));
    shared_ptr<TServerTransport> serverTransport(new TServerSocket(port));
    shared_ptr<TTransportFactory> transportFactory(new TBufferedTransportFactory());
    shared_ptr<TProtocolFactory> protocolFactory = getProtocolFactory(settings);
  
    TSimpleServer server(processor, serverTransport, transportFactory, protocolFactory);

    cout << "Server initialized, preparing to serve..." << endl;
    server.serve();
  }

private:  // implementation of MetricsServiceIf
};

//
// Implementation of public server methods
//


Server::Server(const shared_ptr<Settings>& settings) {
  this->impl = new _ServerImpl(settings);
}

Server::~Server() {
  delete this->impl;
}

void Server::run() {
  this->impl->run();
}

} // /rockserver

