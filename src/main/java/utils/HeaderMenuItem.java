package utils;

import lombok.Getter;

@Getter
public enum HeaderMenuItem {

    HOME("HOME", "//a[@href='/home']"),
    ABOUT("ABOUT", "//a[@href='/about']"),
    LOGIN("LOGIN", "//a[@href='/login']"),
    CONTACTS("CONTACTS", "//a[@href='/contacts']"),
    ADD("ADD", "//a[@href='/add']"),
    SIGNOUT("SIGNOUT", "//button[contains(text(), 'Sign Out')]");

    private final String name;
    private final String locator;

    HeaderMenuItem(String name, String locator) {
        this.name = name;
        this.locator = locator;
    }

    @Override
    public String toString() {
        return name;
    }
}

