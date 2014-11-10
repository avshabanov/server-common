
#pragma once

#include "thrift/gen-cpp/settings_types.h"
#include "thrift/gen-cpp/MetricsService.h"

#include <boost/shared_ptr.hpp>

namespace rockserver {

/**
 * Creates service handler for operating with metrics.
 */
boost::shared_ptr<metrics::MetricsServiceIf> createMetricsServiceHandler(const boost::shared_ptr<rockserver::settings::Settings>& settings);

} // /rockserver

