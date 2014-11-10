
#include "metrics_service.h"

#include <iostream>

#include "rocksdb/db.h"
#include "rocksdb/slice.h"
#include "rocksdb/options.h"

#include <thrift/protocol/TCompactProtocol.h>
#include <thrift/transport/TBufferTransports.h>

using ::std::cout;
using ::std::endl;
using ::std::string;
using ::boost::shared_ptr;
using ::rockserver::settings::Settings;
using ::metrics::MetricsServiceIf;
using ::metrics::MetricsEntry;
using ::metrics::MetricsEntryList;

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;

using namespace rocksdb;

namespace rockserver {

/**
 * Macro for checking rocksdb status codes.
 */
#define CHECK_DB_STATUS(status, message) \
  if (!(status).ok()) {\
    throw new std::logic_error(string("ROCKSDB Error: ") + string(message) + " status=" + (status).ToString());\
  }

static string toString(const MetricsEntry& entry) {
  // TODO: don't make it local
  auto buffer = shared_ptr<TMemoryBuffer>(new TMemoryBuffer());
  auto protocol = shared_ptr<TProtocol>(new TCompactProtocol(buffer));

  entry.write(protocol.get());

  return buffer->getBufferAsString();
}

static shared_ptr<MetricsEntry> entryFrom(const Slice& str) {
  auto charBuf = const_cast<char *>(str.data());
  auto buffer = shared_ptr<TMemoryBuffer>(new TMemoryBuffer((uint8_t *) charBuf, str.size()));
  auto protocol = shared_ptr<TProtocol>(new TCompactProtocol(buffer));

  shared_ptr<MetricsEntry> e(new MetricsEntry());

  e->read(protocol.get());

  return e;
}

/**
 * Internal metrics service handler.
 * TODO: put into separate file.
 */
class MetricsServerHandler : public MetricsServiceIf {
private:
  shared_ptr<Settings> settings;
  shared_ptr<DB> db;

public:
  MetricsServerHandler(const shared_ptr<Settings>& s)
    : db(nullptr) {
      Options options;
      // Optimizations:
      // options.IncreaseParallelism();
      // options.OptimizeLevelStyleCompaction();
      options.create_if_missing = true;
      DB * database;
      Status dbStatus = DB::Open(options, s->dbPath, &database);
      CHECK_DB_STATUS(dbStatus, string("Unable to open db, path=") + s->dbPath);
      this->db = shared_ptr<DB>(database);
      this->settings = s;
  }

  void record(const MetricsEntry& entry) {
    string buf = toString(entry);
    Status s = db->Put(WriteOptions(), entry.origin, buf);
    CHECK_DB_STATUS(s, "Write entry to db");
    cout << "Entry recorded, origin=" << entry.origin << endl; // TODO: log
  }

  void getRecordedEntries(MetricsEntryList& _return, const int32_t limit, const string& lastElementSeed) {
    cout << "Recorded entries" << endl; // TODO: log

    auto it = shared_ptr<Iterator>(db->NewIterator(ReadOptions()));
    std::vector<MetricsEntry> entries;
    for (it->SeekToFirst(); it->Valid(); it->Next()) {
      auto slice = it->value();
      CHECK_DB_STATUS(it->status(), "Error while reading value from db");

      auto entry = entryFrom(slice);
      cout << "entry.origin=" << entry->origin << endl;
      entries.push_back(*entry.get());
    }

    _return.__set_entries(entries);
    _return.__set_lastElementSeed("<None>");
  }
};

/**
 * Factory method for MetricsServiceHandler
 */
shared_ptr<MetricsServiceIf> createMetricsServiceHandler(const shared_ptr<Settings>& settings) {
  return shared_ptr<MetricsServiceIf>(new MetricsServerHandler(settings));
}

} // /rockserver


