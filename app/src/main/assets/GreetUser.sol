// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract GreetUser {
    string private username;

    function setUsername(string memory _name) public {
        username = _name;
    }

    function greet() public view returns (string memory) {
        return string(abi.encodePacked("Hello, ", username, "!"));
    }
}
