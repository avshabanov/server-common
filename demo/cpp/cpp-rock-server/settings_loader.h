
#pragma once

#include "thrift/gen-cpp/settings_constants.h"
#include "thrift/gen-cpp/settings_types.h"

#include <boost/shared_ptr.hpp>

boost::shared_ptr<rockserver::settings::Settings> loadSettings();

void dumpSettings();


