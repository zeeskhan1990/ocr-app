import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { OcrDocumentModule } from './document/document.module';
import { OcrMetadataModule } from './metadata/metadata.module';
import { OcrMetadataTypeModule } from './metadata-type/metadata-type.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        OcrDocumentModule,
        OcrMetadataModule,
        OcrMetadataTypeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OcrEntityModule {}
