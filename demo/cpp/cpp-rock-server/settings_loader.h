
#pragma once

#include "thrift/gen-cpp/settings_constants.h"
#include "thrift/gen-cpp/settings_types.h"

#include <boost/shared_ptr.hpp>

boost::shared_ptr<rockserver::settings::Settings> loadSettings(const std::string& fileName);

void dumpSettings(const std::string& fileName);


