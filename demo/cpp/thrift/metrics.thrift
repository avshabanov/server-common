

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


