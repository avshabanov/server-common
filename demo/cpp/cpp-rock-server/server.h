
#pragma once

#include "thrift/gen-cpp/settings_constants.h"
#include "thrift/gen-cpp/settings_types.h"

#include <boost/shared_ptr.hpp>

namespace rockserver {

/**
 * Hidden implementation.
 */
class _ServerImpl;

/**
 * Public server interface.
 */
class Server {
private:
  _ServerImpl * impl;
public:
  Server(const boost::shared_ptr<rockserver::settings::Settings>& settings);
  ~Server();

  void run();
};

} // rockserver

