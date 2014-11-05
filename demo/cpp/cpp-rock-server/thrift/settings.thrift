
namespace cpp rockserver.settings

enum ProtocolType {
  BINARY = 1,
  COMPACT = 2,
  DENSE = 3,
  ZLIB = 4
}

struct Settings {
  1: required string dbPath
  2: required i32 portNumber
  3: required ProtocolType protocolType = ProtocolType.COMPACT
  4: optional string serverLogPath
}


