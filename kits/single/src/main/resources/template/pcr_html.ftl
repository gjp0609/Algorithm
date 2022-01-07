<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>PCR Rank</title>
    <style>
        body {
            margin: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            user-select: none;
        }

        .main {
            overflow: auto;
            height: 99vh;
        }

        .main * {
            font-family: 'Fira Code', 'Consolas', 'Microsoft YaHei', monospace;
            font-size: 0.8rem;
            box-sizing: border-box;
        }

        table {
            border-collapse: collapse;
            table-layout: fixed;
            width: 99%;
        }

        th,
        td {
            width: 2rem;
            border: #000000 solid 0.01rem;
            text-align: center;
            word-break: keep-all;
            white-space: nowrap;
        }

        td:first-child,
        th:first-child {
            position: sticky;
            left: -1px;
            z-index: 300;
            background-color: white;
            border-left: none;
        }

        th {
            height: 2rem;
            background-color: lightpink;
            position: sticky;
            top: -1px;
            z-index: 400;
        }

        th:first-child {
            z-index: 500;
            background-color: lightpink;
        }

        .image {
            max-width: 34px;
        }

        .name {
            width: 7rem;
            height: 1rem;
        }

        tbody .name {
            transform: scaleX(0.9);
        }

        .split {
            background-color: #aaa;
            width: 0;
        }

        tbody .rank {
            border-right: #aaa solid 0.01rem;
        }

        .rank.god {
            background-color: #f99;
        }

        .rank.ss {
            background-color: #fbb;
        }

        .rank.s {
            background-color: #fdd;
        }

        .rank.a {
            background-color: #fed;
        }

        .rank.b {
            background-color: #ffe;
        }

        .rank.c {
            background-color: #eee;
        }
    </style>
</head>
<body>
<div class="main">
    <table>
        <thead>
        <tr>
            <th class="image">IMG</th>
            <th class="id">ID</th>
            <th class="name">NAME</th><#list ranks as rank>
            <th class="rank">${rank}</th></#list>
            <th class="split"></th><#list ranks as rank>
            <th class="rank">${rank}</th></#list>
        </tr>
        </thead>
        <tbody>
        <#list datas?keys as key>
            <tr>
                <td class="image"><img src="images/${key}.png" width="30" height="30" alt=""/></td>
                <#-- <td class="image"><img src="data:image/png;base64,${datas[key].imgSrc}" width="30" height="30" alt=""/></td> -->
                <td class="id">${datas[key].position}</td>
                <td class="name">${datas[key].name}</td><#list datas[key].ghz as ghz>
                <td class="rank"><#if ghz??>${ghz}<#else></#if></td></#list>
                <td class="split"></td><#list datas[key].jjc as jjc>
                <td class="rank"><#if jjc??>${jjc}<#else></#if></td></#list>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
<script>
    for (const td of document.getElementsByTagName('td')) {
        if (td.innerText.indexOf('神') >= 0) {
            td.className += ' god'
        } else if (td.innerText.indexOf('SS') >= 0) {
            td.className += ' ss'
        } else if (td.innerText.indexOf('S') >= 0) {
            td.className += ' s'
        } else if (td.innerText.indexOf('A') >= 0) {
            td.className += ' a'
        } else if (td.innerText.indexOf('B') >= 0) {
            td.className += ' b'
        } else if (td.innerText.indexOf('C') >= 0) {
            td.className += ' c'
        } else {
            td.className += ''
        }
    }
</script>
</html>