import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-layout',
  imports: [CommonModule, ReactiveFormsModule, RouterModule], // ✅ Fix here
  templateUrl: './layout.html',
  styleUrl: './layout.scss'
})
export class Layout {

}
