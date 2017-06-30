import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { MetadataType } from './metadata-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MetadataTypeService {

    private resourceUrl = 'api/metadata-types';

    constructor(private http: Http) { }

    create(metadataType: MetadataType): Observable<MetadataType> {
        const copy = this.convert(metadataType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(metadataType: MetadataType): Observable<MetadataType> {
        const copy = this.convert(metadataType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<MetadataType> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(metadataType: MetadataType): MetadataType {
        const copy: MetadataType = Object.assign({}, metadataType);
        return copy;
    }
}
