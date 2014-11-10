
## Save Default Settings (can be overriden afterwards)

```
./target/rockserver -sf /tmp/settings.json -is
```

This will create file ``/tmp/settings.json``.

## Dump Settings

```
./target/rockserver -sf /tmp/settings.json -ds
```

## Start Test Server

```
./target/rockserver -sf /tmp/settings.json -s
```

## Run Demo Client


Record demo metrics

```
./target/rockserver -sf /tmp/settings.json -c record
```

Dump all recorded metrics

```
./target/rockserver -sf /tmp/settings.json -c record
```


