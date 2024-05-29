import Keycloak from "keycloak-js";
const keycloak = new Keycloak({
 url: "http://192.168.1.119:9864/auth",
 realm: "Andante",
 clientId: "react-web-app"
});

export default keycloak;