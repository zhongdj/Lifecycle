package net.madz.common;

import net.madz.common.DottedPath;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DottedPathTest {

    @Test
    public void should_know_dotted_path_details_for_one_level() {
        DottedPath path = DottedPath.parse("a");
        assertThat(path.size(), is(1));
        assertThat(path.getAbsoluteName(), is("a"));
        assertThat(path.getName(), is("a"));
    }

    @Test
    public void should_know_the_parent_of_one_level_is_empty() {
        DottedPath path = DottedPath.parse("a");
        assertThat(path.getParent().isPresent(), is(Boolean.FALSE));
    }

    @Test
    public void should_know_dotted_path_details_for_two_levels() {
        DottedPath path = DottedPath.parse("a.b");
        assertThat(path.size(), is(2));
        assertThat(path.getName(), is("b"));
        assertThat(path.getAbsoluteName(), is("a.b"));
    }

    @Test
    public void should_know_dotted_path_details_for_multiple_levels() {
        DottedPath path = DottedPath.parse("a.b.c.d");
        assertThat(path.size(), is(4));
        assertThat(path.getName(), is("d"));
        assertThat(path.getAbsoluteName(), is("a.b.c.d"));
        assertThat(path.getParent().get().getAbsoluteName(), is("a.b.c"));
    }
}