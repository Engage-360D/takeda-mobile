//
//  AddIncident.m
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "AddIncident.h"
#import "LeftMenu.h"

@interface AddIncident (){
    NSDictionary *alerts;
    NSMutableDictionary *localIncidents;
}

@end

int incident;

@implementation AddIncident

- (void)viewDidLoad {
    [super viewDidLoad];
    alerts = @{@"hadHeartAttackOrStroke":   @{@"text":@"Если вы перенесли инфаркт, вам НЕОБХОДИМО соблюдать рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               @"hadBypassSurgery":         @{@"text":@"Если вы перенесли коронарное шунтирование, вам НЕОБХОДИМО соблюдать рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               @"hasDiabetes":              @{@"text":@"Если вы перенесли диабет, вам НЕОБХОДИМО соблюдать рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"} };

    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    localIncidents = [[Global recursiveMutable: User.incidents] mutableCopy];
    [self showInfo];
}

-(void)setupInterface{
    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItem = [self menuBarBtnWithTitle:@"Готово" selector:@selector(save) forTarget:self];
    self.incidentsContainer.backgroundColor = [UIColor clearColor];
    _addComment.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    [_addComment setupPlaceholder:@"Добавьте комментарий" withFont:_addComment.font];
    for (UIButton *btn in self.incBtns){
        btn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    }
    for (UILabel *lb in self.blockLabels){
        lb.font = [UIFont fontWithName:@"SegoeWP-Semibold" size:14];
    }

    [self.scrollView setup_autosize];
}


-(IBAction)addIncidentAction:(UIButton*)sender{
    localIncidents = [NSMutableDictionary new];
    incident = sender.tag;
    for (UIButton *btn in self.incBtns){
        btn.selected = (btn == sender);
    }
    [GData deleteAllIncidents:localIncidents];
    [GData addIncidentTo:localIncidents incident:[[GlobalData incidents] objectForKey:[NSNumber numberWithInt:sender.tag]] comment:_addComment.text];
    //[GData setIncidentTo:localIncidents incident:sender.tag comment:_addComment.text];
    [self showInfo];
}

-(void)showInfo{
    [self.incidentsContainer removeSubviews];
    for (NSString *key in localIncidents.allKeys){
        if ([localIncidents[key] boolValue]){
            
            RAlertView *rAV = [[RAlertView alloc] init];
            [self.incidentsContainer addSubview:rAV];
            //  [rAV setupWithTitle:alerts[dict[@"type"]][@"title"] text:alerts[dict[@"type"]][@"text"] img:alerts[dict[@"type"]][@"image"]];
            [rAV setupWithTitle:alerts[key][@"title"] text:alerts[key][@"text"] img:alerts[key][@"image"]];
            
        }
        
    }
    
    [self.incidentsContainer setupAutosizeBySubviews];
    [self.scrollView setup_autosize];
}

-(void)save{
    [self showActivityIndicatorWithString:@""];
    [ServData sendIncident:[[GlobalData incidents] objectForKey:[NSNumber numberWithInt:incident]] comment:(_addComment.text) completion:^(BOOL success, NSError *error, id res){
        [self removeActivityIdicator];
        if (success){
            
            [GData deleteAllIncidents:localIncidents];
            [GData addIncidentTo:localIncidents incident:[[GlobalData incidents] objectForKey:[NSNumber numberWithInt:incident]] comment:_addComment.text];
            
            User.incidents = [[Global recursiveMutable:localIncidents] mutableCopy];
            [User saveIncidents];
            
            [self showMessage:@"Инцидент успешно добавлен" title:@"Успех" result:^{
//                [self.navigationController popToRootViewControllerAnimated:NO];
                [[rootMenuController sharedInstance].leftMenu openScreenOnIncidentFrom:self.slideMenuController];
                //[self backAction];
            }];
        } else {
            [self showMessage:@"Не удалось добавить инцидент" title:@"Ошибка"];
        }
    }];
}



@end
