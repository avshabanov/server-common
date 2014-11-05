
#pragma once

#include "arg_parser.h"

#include "thrift/gen-cpp/settings_constants.h"
#include "thrift/gen-cpp/settings_types.h"

#include <boost/shared_ptr.hpp>

void runDemoClient(const boost::shared_ptr<rockserver::settings::Settings>& settings, ArgParser& ap);

