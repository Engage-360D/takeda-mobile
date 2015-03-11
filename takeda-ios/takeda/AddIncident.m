//
//  AddIncident.m
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "AddIncident.h"

@interface AddIncident (){
    NSDictionary *alerts;
    NSMutableArray *localIncidents;
}

@end

int incident;

@implementation AddIncident

- (void)viewDidLoad {
    [super viewDidLoad];
    alerts = @{[NSNumber numberWithInt:inInsultInfarct]:@{@"text":@"Вы перенесли Инфракт/Инсульт и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               [NSNumber numberWithInt:inCoronar]:@{@"text":@"Вы перенесли Коронарное шунтирование и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               [NSNumber numberWithInt:inDiabet]:@{@"text":@"Вы перенесли Диабет и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"} };

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
    localIncidents = [NSMutableArray new];
    incident = sender.tag;
    for (UIButton *btn in self.incBtns){
        btn.selected = (btn == sender);
    }
    [GData setIncidentTo:localIncidents incident:sender.tag comment:_addComment.text];
    [self showInfo];
}

-(void)showInfo{
    [self.incidentsContainer removeSubviews];
    for (NSMutableDictionary *dict in localIncidents){
            RAlertView *rAV = [[RAlertView alloc] init];
            [self.incidentsContainer addSubview:rAV];
            [rAV setupWithTitle:alerts[dict[@"type"]][@"title"] text:alerts[dict[@"type"]][@"text"] img:alerts[dict[@"type"]][@"image"]];
    }
    
    [self.incidentsContainer setupAutosizeBySubviews];
    [self.scrollView setup_autosize];
}

-(void)save{
    [ServData sendIncident:[[GlobalData incidents] objectForKey:[NSNumber numberWithInt:incident]] comment:(_addComment.text) completion:^(BOOL success, NSError *error, id res){
        if (success){
            [GData setIncidentTo:localIncidents incident:incident comment:_addComment.text];
            User.incidents = [[Global recursiveMutable:localIncidents] mutableCopy];
            [User saveIncidents];
            [self showMessage:@"Инцидент успешно добавлен" title:@"Успех" result:^{
                [self backAction];
            }];
        } else {
            [self showMessage:@"Не удалось добавить инцидент" title:@"Ошибка"];
        }
    }];
}



@end
