import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { DocumentComponent } from './document.component';
import { DocumentDetailComponent } from './document-detail.component';
import { DocumentPopupComponent } from './document-dialog.component';
import { DocumentDeletePopupComponent } from './document-delete-dialog.component';

import { Principal } from '../../shared';

export const documentRoute: Routes = [
    {
        path: 'document',
        component: DocumentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Documents'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'document/:id',
        component: DocumentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Documents'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const documentPopupRoute: Routes = [
    {
        path: 'document-new',
        component: DocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Documents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'document/:id/edit',
        component: DocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Documents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'document/:id/delete',
        component: DocumentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Documents'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
