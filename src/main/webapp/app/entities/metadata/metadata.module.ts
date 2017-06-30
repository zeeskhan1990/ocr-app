import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OcrSharedModule } from '../../shared';
import {
    MetadataService,
    MetadataPopupService,
    MetadataComponent,
    MetadataDetailComponent,
    MetadataDialogComponent,
    MetadataPopupComponent,
    MetadataDeletePopupComponent,
    MetadataDeleteDialogComponent,
    metadataRoute,
    metadataPopupRoute,
} from './';

const ENTITY_STATES = [
    ...metadataRoute,
    ...metadataPopupRoute,
];

@NgModule({
    imports: [
        OcrSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MetadataComponent,
        MetadataDetailComponent,
        MetadataDialogComponent,
        MetadataDeleteDialogComponent,
        MetadataPopupComponent,
        MetadataDeletePopupComponent,
    ],
    entryComponents: [
        MetadataComponent,
        MetadataDialogComponent,
        MetadataPopupComponent,
        MetadataDeleteDialogComponent,
        MetadataDeletePopupComponent,
    ],
    providers: [
        MetadataService,
        MetadataPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OcrMetadataModule {}
