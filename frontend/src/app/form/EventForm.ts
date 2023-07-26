import {UserForm} from "./UserForm";

export class EventForm{
    constructor(public name: string,
                public user: UserForm,
                public id?: number
    ) { }
}
