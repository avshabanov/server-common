
#pragma once

#include <vector>
#include <exception>
#include <cstring>

/**
 * Helper parser for command-line arguments.
 */
class ArgParser {
private:
  char ** argv;
  char ** argvEnd;

  void ensureHaveArgs() {
    if (argv == argvEnd) {
      throw new std::logic_error("One more argument expected");
    }
  }

public:
  ArgParser(int argc, char ** argv) {
    this->argv = argv;
    this->argvEnd = this->argv + (argc - 1);
  }

  const char * next() {
    ensureHaveArgs();
    ++argv;
    return arg();
  }

  const char * arg() const {
    return *argv;
  }

  bool isArg(const std::vector<const char *>& args) const {
    for (auto it = args.begin(); it != args.end(); ++it) {
      if (0 == strcmp(arg(), *it)) {
        return true;
      }
    }
    return false;
  }

  bool isArg(const char * arg1) const {
    return isArg(std::vector<const char *>({ arg1 }));
  }

  bool isArg(const char * arg1, const char * arg2) const {
    return isArg(std::vector<const char *>({ arg1, arg2 }));
  }
};




