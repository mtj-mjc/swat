import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  hide = true;

  constructor(private router: Router, private authenticationService: AuthenticationService) {}

  ngOnInit() {}

  onLogin(username: string, password: string) {
    console.log(username);
    console.log(password);
    if (this.authenticationService.login(username, password)) {
      this.router.navigate(['/']);
    }
  }
}
