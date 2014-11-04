
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

.PHONY: clean thrift-gen dirs

target/mcli: $(TO)/main.o $(TO)/metrics_constants.o $(TO)/metrics_types.o $(TO)/settings_types.o $(TO)/settings_constants.o $(TO)/settings_loader.o
	$(LINKER) -o target/mcli $(TO)/*.o $(LFLAGS)

# Thrift Generated Sources Compilation

$(TO)/metrics_constants.o: $(TG)/metrics_constants.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/metrics_constants.cpp -o $(TO)/metrics_constants.o

$(TO)/metrics_types.o: $(TG)/metrics_types.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/metrics_types.cpp -o $(TO)/metrics_types.o

$(TO)/settings_constants.o: $(TG)/settings_constants.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/settings_constants.cpp -o $(TO)/settings_constants.o

$(TO)/settings_types.o: $(TG)/settings_types.cpp
	$(CXX) $(CXX_FLAGS) -c $(TG)/settings_types.cpp -o $(TO)/settings_types.o

# Main Sources Compilation

$(TO)/main.o: thrift-gen main.cc settings_loader.h
	$(CXX) $(CXX_FLAGS) -c main.cc -o $(TO)/main.o

$(TO)/settings_loader.o: thrift-gen settings_loader.cc settings_loader.h
	$(CXX) $(CXX_FLAGS) -c settings_loader.cc -o $(TO)/settings_loader.o


# Thrift Object Generation
#
thrift-gen: dirs ../thrift/metrics.thrift thrift/settings.thrift
	$(THRIFT) -o target/gen/thrift --gen cpp ../thrift/metrics.thrift
	$(THRIFT) -o target/gen/thrift --gen cpp thrift/settings.thrift

# Object files (obj) and generated sources (gen)
dirs:
	mkdir -p target/obj
	mkdir -p target/gen/thrift

clean:
	rm -rf target


