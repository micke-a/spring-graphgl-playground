<#macro page title="GraphQL Playground">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - GraphQL Playground</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>
    <script>
        async function graphqlFetch(query, variables) {
            const response = await fetch('/graphql', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ query, variables })
            });
            const json = await response.json();
            if (json.errors) {
                throw new Error(json.errors.map(e => e.message).join(', '));
            }
            return json.data;
        }
    </script>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container">
            <a class="navbar-brand" href="/">GraphQL Playground</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/customers">Customers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/accounts">Accounts</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/cards">Cards</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/graphiql">GraphiQL</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <#nested>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
</#macro>
