

var csrfCookieAuth = new SwaggerClient.ApiKeyAuthorization("X-XSRF-TOKEN", '81ed8cd3-4b5e-4be1-8ea9-272ae7cc6b7c', "header");
        swaggerUi.api.clientAuthorizations.add("X-XSRF-TOKEN", csrfCookieAuth);