
#include <iostream>
#include <string>
#include <cstring>

#include <unistd.h>
#include <fcntl.h>

#include <rocksdb/db.h>

#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TCompactProtocol.h>

#include <thrift/transport/TTransportUtils.h>
#include <thrift/transport/TFDTransport.h>

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

#if 1

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


//
// Helper functions
//

static inline int usage(char * argv[], int code) {
  cerr << "Usage: " << argv[0] << " w|r" << endl;
  return code;
}

//
// Entry point
//

int main(int argc, char * argv[]) {
  if (argc < 5) {
    std::string str = writeEntryToString();
    readEntryFromString(str);
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

static int old_main(int argc, char* argv[]) {
  // old
 
  if (strcmp(argv[1], "r") == 0) {
    int fd = open(argv[2], O_RDONLY);
    if (fd < 0) {
      return errm("Failed to open a file");
    }

    shared_ptr<TFDTransport> innerTransport(new TFDTransport(fd));
    shared_ptr<TBufferedTransport> transport(new TBufferedTransport(innerTransport));

    shared_ptr<TBinaryProtocol> protocol(new TBinaryProtocol(transport));

    transport->open();

    metrics::Header header;
    header.read(protocol.get());

    transport->close();

    cout << "OK: read from file succeeded, header.contentLength=" << header.contentLength << ", header.contentType=" << header.contentType  << endl;
  } else if (strcmp(argv[1], "w") == 0) {
    int fd = open(argv[2], O_CREAT | O_TRUNC | O_WRONLY, S_IRUSR | S_IWUSR | S_IXUSR);
    if (fd < 0) {
      return errm("Failed to open a file");
    }
    shared_ptr<TFDTransport> innerTransport(new TFDTransport(fd));
    shared_ptr<TBufferedTransport> transport(new TBufferedTransport(innerTransport));

    shared_ptr<TBinaryProtocol> protocol(new TBinaryProtocol(transport));

    metrics::Header header;
    header.__set_contentLength(123);
    header.__set_contentType("test");

    header.write(protocol.get());

    transport->close();

    cout << "OK: write to file succeeded" << endl;
  } else {
    return usage(argv);
  }
} // /old_main

#endif

