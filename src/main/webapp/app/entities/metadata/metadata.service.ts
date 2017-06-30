import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Metadata } from './metadata.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MetadataService {

    private resourceUrl = 'api/metadata';

    constructor(private http: Http) { }

    create(metadata: Metadata): Observable<Metadata> {
        const copy = this.convert(metadata);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(metadata: Metadata): Observable<Metadata> {
        const copy = this.convert(metadata);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Metadata> {
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

    private convert(metadata: Metadata): Metadata {
        const copy: Metadata = Object.assign({}, metadata);
        return copy;
    }
}
