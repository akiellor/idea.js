idea.js
=======

'idea.js' is an intellij plugin which gives developers the ability to define
context sensitive run configurations.

Example
-------

The following example would run any file which ends with 'application.rb' as
JRuby script.

```javascript
//<project-root>/idea.js

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
```