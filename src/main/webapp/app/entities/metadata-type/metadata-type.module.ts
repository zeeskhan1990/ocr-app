import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OcrSharedModule } from '../../shared';
import {
    MetadataTypeService,
    MetadataTypePopupService,
    MetadataTypeComponent,
    MetadataTypeDetailComponent,
    MetadataTypeDialogComponent,
    MetadataTypePopupComponent,
    MetadataTypeDeletePopupComponent,
    MetadataTypeDeleteDialogComponent,
    metadataTypeRoute,
    metadataTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...metadataTypeRoute,
    ...metadataTypePopupRoute,
];

@NgModule({
    imports: [
        OcrSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MetadataTypeComponent,
        MetadataTypeDetailComponent,
        MetadataTypeDialogComponent,
        MetadataTypeDeleteDialogComponent,
        MetadataTypePopupComponent,
        MetadataTypeDeletePopupComponent,
    ],
    entryComponents: [
        MetadataTypeComponent,
        MetadataTypeDialogComponent,
        MetadataTypePopupComponent,
        MetadataTypeDeleteDialogComponent,
        MetadataTypeDeletePopupComponent,
    ],
    providers: [
        MetadataTypeService,
        MetadataTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OcrMetadataTypeModule {}
