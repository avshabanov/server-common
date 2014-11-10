
CXX_FLAGS					= -std=c++11 -Wall -pedantic -DHAVE_INTTYPES_H -DHAVE_NETINET_IN_H -Itarget/gen
CXX								= g++
LFLAGS						= -lrocksdb -lthrift
LINKER						= g++

# Folder with object files
TO								= target/obj

# Folder with generated thrift classes
TG								= target/gen/thrift/gen-cpp

THRIFT						= thrift

## Rules

.PHONY: clean

all: target/rockserver

target/rockserver: $(TO)/main.o $(TO)/metrics_constants.o $(TO)/metrics_types.o $(TO)/settings_types.o $(TO)/settings_constants.o $(TO)/settings_loader.o $(TO)/server.o $(TO)/MetricsService.o $(TO)/demo_client.o $(TO)/metrics_service.o
	$(LINKER) -o target/rockserver $(TO)/*.o $(LFLAGS)

# Compiling Thrift Generated Sources

$(TO)/metrics_constants.o: $(TG)/metrics_constants.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/metrics_constants.cpp -o $(TO)/metrics_constants.o

$(TO)/metrics_types.o: $(TG)/metrics_types.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/metrics_types.cpp -o $(TO)/metrics_types.o

$(TO)/MetricsService.o: $(TG)/MetricsService.cpp
	  $(CXX) $(CXX_FLAGS) -c $(TG)/MetricsService.cpp -o $(TO)/MetricsService.o

$(TO)/settings_constants.o: $(TG)/settings_constants.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/settings_constants.cpp -o $(TO)/settings_constants.o

$(TO)/settings_types.o: $(TG)/settings_types.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/settings_types.cpp -o $(TO)/settings_types.o

# Main Sources Compilation

$(TO)/main.o: target/lastgen main.cc settings_loader.h arg_parser.h demo_client.h
	$(CXX) $(CXX_FLAGS) -c main.cc -o $(TO)/main.o

$(TO)/settings_loader.o: target/lastgen settings_loader.cc settings_loader.h
	$(CXX) $(CXX_FLAGS) -c settings_loader.cc -o $(TO)/settings_loader.o

$(TO)/server.o: target/lastgen server.cc server.h metrics_service.h
	$(CXX) $(CXX_FLAGS) -c server.cc -o $(TO)/server.o

$(TO)/metrics_service.o: target/lastgen metrics_service.cc metrics_service.h
	$(CXX) $(CXX_FLAGS) -c metrics_service.cc -o $(TO)/metrics_service.o

$(TO)/demo_client.o: target/lastgen demo_client.cc demo_client.h arg_parser.h
	$(CXX) $(CXX_FLAGS) -c demo_client.cc -o $(TO)/demo_client.o


# Thrift Object Generation

target/lastgen: target/gen/thrift ../thrift/metrics.thrift thrift/settings.thrift
	$(THRIFT) -o target/gen/thrift --gen cpp ../thrift/metrics.thrift
	$(THRIFT) -o target/gen/thrift --gen cpp thrift/settings.thrift
	touch target/lastgen

# Object files (obj) and generated sources (gen)

target/gen/thrift: target/obj
	mkdir -p target/gen/thrift

target/obj:
	mkdir -p target/obj

clean:
	rm -rf target

