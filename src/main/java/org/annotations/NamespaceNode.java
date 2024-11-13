package org.annotations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamespaceNode {
    private String _path;
    private Set<NamespaceNode> _children;
    private boolean _matchPath = false;

    public NamespaceNode(String path) {}
    public NamespaceNode(String path, HashSet<NamespaceNode> children, boolean matchPath) {
        _path = path;
        _children = children;
        _matchPath = matchPath;
    }

    public String getPath() {
        return _path;
    }

    public Set<NamespaceNode> getChildren() {
        return _children;
    }

    public boolean isMatchPath() {
        return _matchPath;
    }

    @Override
    public int hashCode(){
        return _path.hashCode();
    }
}
