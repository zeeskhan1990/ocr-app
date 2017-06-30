
const enum ExtractionStatus {
    'PENDING',
    'VERIFIED'

};
import { Metadata } from '../metadata';
import { User } from '../../shared';
export class Document {
    constructor(
        public id?: number,
        public documentBytes?: any,
        public extractionStatus?: ExtractionStatus,
        public metadata?: Metadata,
        public user?: User,
    ) {
    }
}
