import { MetadataType } from '../metadata-type';
import { Document } from '../document';
export class Metadata {
    constructor(
        public id?: number,
        public originalValue?: string,
        public verifiedValue?: string,
        public metadataType?: MetadataType,
        public document?: Document,
    ) {
    }
}
