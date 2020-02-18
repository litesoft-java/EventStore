package org.litesoft.events;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final String mVersion;

    public HomeController( VersionSupplier pVersionSupplier ) {
        mVersion = pVersionSupplier.get();
    }

    @GetMapping(value = "/")
    public String index() {
        return "Hello (From EventStore vs " + mVersion + ")";
    }
}
