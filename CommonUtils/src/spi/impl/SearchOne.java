package spi.impl;

import spi.Search;

public class SearchOne implements Search {
    @Override
    public String search(String content) {
        return "Response of Search One: " + content;
    }
}
