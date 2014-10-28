
CXX_FLAGS					= -std=c++11 -Wall -pedantic -Itarget/gen
CXX								= g++
LFLAGS						= -lrocksdb -lprotobuf
LINKER						= g++

PROTOC						= protoc

## Rules

target/mcli: target/obj/main.o target/obj/metrics.pb.o
	$(LINKER) -o target/mcli target/obj/main.o target/obj/metrics.pb.o $(LFLAGS)

target/obj/metrics.pb.o:
	$(CXX) $(CXX_FLAGS) -c target/gen/metrics.pb.cc -o target/obj/metrics.pb.o

target/obj/main.o: target/obj main.cc target/gen/metrics.pb.cc
	$(CXX) $(CXX_FLAGS) -c main.cc -o target/obj/main.o

target/gen/metrics.pb.cc: target/gen proto/metrics.proto
	$(PROTOC) -I=proto/ --cpp_out=target/gen/ proto/metrics.proto

# Object files
target/obj: target
	mkdir target/obj

# Generated code dir
target/gen: target
	mkdir target/gen

target:
	mkdir target

clean:
	rm -rf target


