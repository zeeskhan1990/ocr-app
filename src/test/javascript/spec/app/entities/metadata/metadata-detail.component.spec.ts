import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { OcrTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MetadataDetailComponent } from '../../../../../../main/webapp/app/entities/metadata/metadata-detail.component';
import { MetadataService } from '../../../../../../main/webapp/app/entities/metadata/metadata.service';
import { Metadata } from '../../../../../../main/webapp/app/entities/metadata/metadata.model';

describe('Component Tests', () => {

    describe('Metadata Management Detail Component', () => {
        let comp: MetadataDetailComponent;
        let fixture: ComponentFixture<MetadataDetailComponent>;
        let service: MetadataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OcrTestModule],
                declarations: [MetadataDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MetadataService,
                    EventManager
                ]
            }).overrideTemplate(MetadataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MetadataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetadataService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Metadata(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.metadata).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
