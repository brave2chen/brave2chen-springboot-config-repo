package spi;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) {
        Search search = null;
        ServiceLoader<Search> serviceLoader = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = serviceLoader.iterator();
        if(iterator.hasNext()) {
            search = iterator.next();
        }
        Objects.requireNonNull(search, "请通过SPI配置Search的具体实现！");
        System.out.println(search.search("Hello World!"));
    }

}
