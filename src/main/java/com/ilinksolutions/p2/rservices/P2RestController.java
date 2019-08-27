package com.ilinksolutions.p2.rservices;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class P2RestController
{ 
    @GetMapping("/hello")
    public Collection<String> sayHello()
    {
        return IntStream.range(0, 10).mapToObj(i -> "Hello number " + i).collect(Collectors.toList());
    }
}
