import { Component, OnInit } from '@angular/core';
import { Group } from '../../models/group';
import { Store } from '../../models/store';
import { UserService } from '../../services/user.service';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  hide = true;
  groups: Group[];
  stores: Store[];
  groupid: string;
  storeid: string;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getAllGroups().subscribe(res => {
      this.groups = res;
    });
    this.userService.getAllStores().subscribe(res => {
      this.stores = res;
    });
  }

  onSave(name: string, lastname: string, username: string, password: string) {
    const user = new User();
    user.name = name;
    user.lastName = lastname;
    user.username = username;
    user.password = password;
    user.groupid = this.groupid;
    user.storeid = this.storeid;

    this.userService.createUser(user).subscribe(res => {
      console.log(res);
    });
  }
}
