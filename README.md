# calcite-for-sql
使用calcite解析sql语句，输出sql中各个部分。可用于sql血缘分析。

# input
postman   

`post http://localhost:8080/sql/parse`  
```
{
    "sql":"select a.cl as a_cl,b.cl from test a join (select id ,amt from trx) b on a.id = b.id"
}
```

# output
```
{
"selectPhrases": [
{
"sqlKind": "AS",
"expression": "`A`.`CL` AS `A_CL`",
"subQueryPhrase": null,
"subQuery": false
},
{
"sqlKind": "IDENTIFIER",
"expression": "B.CL",
"subQueryPhrase": null,
"subQuery": false
}
],
"fromPhrases": [
{
"sqlKind": "AS",
"joinType": null,
"expression": "`TEST` AS `A`",
"subQueryPhrase": null,
"subQuery": false
},
{
"sqlKind": "AS",
"joinType": "INNER",
"expression": "(SELECT `ID`, `AMT`\r\nFROM `TRX`) AS `B`",
"subQueryPhrase": {
"selectPhrases": [
{
"sqlKind": "IDENTIFIER",
"expression": "ID",
"subQueryPhrase": null,
"subQuery": false
},
{
"sqlKind": "IDENTIFIER",
"expression": "AMT",
"subQueryPhrase": null,
"subQuery": false
}
],
"fromPhrases": [
{
"sqlKind": "IDENTIFIER",
"joinType": null,
"expression": "TRX",
"subQueryPhrase": null,
"subQuery": false
}
]
},
"subQuery": true
}
]
}
```
