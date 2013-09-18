exports.configuration = function(context) {
    var name = context.getLocation().getVirtualFile().getPresentableName();
    var canonicalPath = context.getLocation().getVirtualFile().getCanonicalPath();

    if (/application\.rb$/.test(canonicalPath)) {
        return {
            name: name,
            main: "org.jruby.Main",
            arguments: canonicalPath
        };
    }
}