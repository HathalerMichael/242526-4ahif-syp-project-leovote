import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { CandidateComponent } from './candidate/candidate.component';
import {FormsModule} from "@angular/forms";
import { BlockComponent } from './block/block.component';

@NgModule({
  declarations: [
    AppComponent,
    CandidateComponent,
    BlockComponent
  ],
  imports: [
    BrowserModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
