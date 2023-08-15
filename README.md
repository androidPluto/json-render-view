# JSONREnderView

Utility to render JSON in a readable format. This also supports expand & collpase functionality.

## Integration

#### Add JSONRenderView to xml
```xml
<com.jsonrenderview.JsonRenderView
    android:id="@+id/json"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

#### Apply configuration *(optional)*
```java
binding.json.applyConfig(
    Config.Builder()
        .keyObjectColor(...)
        .keyFieldColor(...)
        .valueNumberColor(...)
        .valueBooleanColor(...)
        .valueStringColor(...)
        .build()
)
```

#### Bind JSON content to view
```java
binding.json.bind(jsonContent)
```
#### Perform actions
Expand & Collapse JSON tree
```java
binding.json.action.expand()
binding.json.action.collapse()
```
