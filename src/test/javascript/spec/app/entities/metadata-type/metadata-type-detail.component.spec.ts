import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { OcrTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MetadataTypeDetailComponent } from '../../../../../../main/webapp/app/entities/metadata-type/metadata-type-detail.component';
import { MetadataTypeService } from '../../../../../../main/webapp/app/entities/metadata-type/metadata-type.service';
import { MetadataType } from '../../../../../../main/webapp/app/entities/metadata-type/metadata-type.model';

describe('Component Tests', () => {

    describe('MetadataType Management Detail Component', () => {
        let comp: MetadataTypeDetailComponent;
        let fixture: ComponentFixture<MetadataTypeDetailComponent>;
        let service: MetadataTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [OcrTestModule],
                declarations: [MetadataTypeDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MetadataTypeService,
                    EventManager
                ]
            }).overrideTemplate(MetadataTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MetadataTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetadataTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MetadataType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.metadataType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
