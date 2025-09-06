import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Layout } from "./components/layout/layout"; // ✅ import your layout

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, Layout], // ✅ include AppLayoutComponent
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'] // ✅ fixed typo
})
export class AppComponent {
  title = 'erp-frontend';
}
