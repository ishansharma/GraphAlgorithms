package ixs171130;

import rbk.Graph.Vertex;

import java.util.Objects;

public class EdgeHandle {

    Vertex from, to;

    public EdgeHandle(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeHandle that = (EdgeHandle) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
