

namespace cpp metrics

struct ExtraDataEntry {
  1: required string name
  2: optional string strValue
  3: optional i32 i32Value
  4: optional i64 i64Value
  5: optional double dblValue
  6: optional bool boolValue
}

struct MetricsEntry {
  1: required string origin
  2: optional i64 startTime
  3: optional i64 timeDelta
  4: optional bool succeeded
  5: optional list<ExtraDataEntry> extraEntries
}


/**
 * Represents a replacement for offset-limit queries.
 */
struct MetricsEntryList {
  1: required list<MetricsEntry> entries;
  2: required string lastElementSeed;
}

exception InvalidQuery {
  1: string fieldCode
  2: string description
}

service MetricsService {

  /**
   * Asynchronously records metrics entry.
   */
  oneway void record(1:MetricsEntry entry);

  /**
   * Returns recorded entries, no more than limit per request.
   */
  MetricsEntryList getRecordedEntries(1:required i32 limit, 2:optional string lastElementSeed) throws (1:InvalidQuery invalidQuery);
}


