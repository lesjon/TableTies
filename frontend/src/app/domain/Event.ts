import KeycloakUser from "./KeycloakUser";

export default interface Event {

  id: number;
  name: string;
  keycloakUser: KeycloakUser;
}
