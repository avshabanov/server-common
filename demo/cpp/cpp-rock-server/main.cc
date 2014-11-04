
#include <iostream>
#include <string>
#include <cstring>

#include "settings_loader.h"

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
// Server code
//


//
// Helper functions
//

static inline int usage(char * argv[], int code) {
  cerr << "Usage: " << argv[0] << " <<TBD>>" << endl;
  return code;
}

//
// Entry point
//

int main(int argc, char * argv[]) {
  if (argc < 5) {
    dumpSettings();
    auto settings = loadSettings();
    cout << "dbPath=" << settings->dbPath << endl;
    return 0;
  }

  if (argc != 3) {
    return usage(argv, 0);
  }

  try {
  } catch (const std::exception& e) {
    cerr << "Error: " << e.what() << endl;
    return usage(argv, -1);
  }

  return 0;
}


#if 0

static void initMetricsEntry(
    metrics::MetricsEntry* value,
    const std::string& origin, int64_t startTime, int64_t timeDelta) {
  value->__set_origin(origin);
  value->__set_startTime(startTime);
  value->__set_timeDelta(timeDelta);
}

static std::string writeEntryToString() {
  auto buffer = shared_ptr<TMemoryBuffer>(new TMemoryBuffer());
//  auto protocol = shared_ptr<TProtocol>(new TBinaryProtocol(buffer));
  auto protocol = shared_ptr<TProtocol>(new TCompactProtocol(buffer));

  metrics::MetricsEntry value;

  initMetricsEntry(&value, "test", 1000, 123);

  value.write(protocol.get());

  std::string str = buffer->getBufferAsString();

  cout << "Serialized. Size = " << str.size() << endl;
  return str;
}

static void readEntryFromString(const std::string& str) {
  char * charBuf = const_cast<char *>(str.c_str());
  auto buffer = shared_ptr<TMemoryBuffer>(new TMemoryBuffer((uint8_t *) charBuf, str.size()));
  auto protocol = shared_ptr<TProtocol>(new TCompactProtocol(buffer));

  metrics::MetricsEntry value;

  value.read(protocol.get());

  cout << "Restored, value: origin=" << value.origin << ", startTime=" << value.startTime << ", timeDelta=" << value.timeDelta << endl;
}

#endif


