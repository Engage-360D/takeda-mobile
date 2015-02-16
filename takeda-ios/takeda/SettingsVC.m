//
//  SettingsVC.m
//  takeda
//
//  Created by Alexander Rudenko on 03.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "SettingsVC.h"

@interface SettingsVC (){
    NSDictionary *alerts;
}
@end

@implementation SettingsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    alerts = @{[NSNumber numberWithInt:inInfarct]:@{@"text":@"Вы перенесли Инфракт и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               [NSNumber numberWithInt:inInsult]:@{@"text":@"Вы перенесли Инсульт и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"},
               [NSNumber numberWithInt:inCoronar]:@{@"text":@"Вы перенесли Коронарное шунтирование и вам следует соблюдать ТОЛЬКО рекомендации вашего лечащего врача",@"image":@"dangerRedIcon",@"title":@"Внимание!"} };
    
    
    [self setupInterface];
    self.mainElement = self.scrollView;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self showInfo];
}

-(void)setupInterface{
    UILabel *tL = [[UILabel alloc] initWithFrame:CGRectMake(_deleteAllResults.titleLabel.x, 15, 200, 15)];
    tL.font = [UIFont fontWithName:@"SegoeWP-Light" size:10];
    tL.textColor = RGB(95, 95, 95);
    tL.backgroundColor = [UIColor clearColor];
    tL.text = @"Для операции необходим пароль";
    
    [_deleteAllResults addSubview:tL];
    _deleteAllResults.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    [_deleteAllResults setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    _logoutBtn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];
    [_logoutBtn setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    _logoutBtn.contentEdgeInsets = UIEdgeInsetsMake(-3, 0, 0, 0);
    _logoutBtn.layer.borderColor = RGB(54, 65, 71).CGColor;
    _logoutBtn.layer.borderWidth = 1;
    _logoutBtn.layer.cornerRadius = 5.0f;
    [_logoutBtn setBackgroundImage:nil forState:UIControlStateNormal];
    _addIncident.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    [_addIncident setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    _incidentsContainer.backgroundColor = [UIColor clearColor];
    for (UILabel *lb in self.blockLabels){
        lb.font = [UIFont fontWithName:@"SegoeWP" size:14];
    }
    [self drawBorders:self.bg_block];

}

-(void)showInfo{
    [self.incidentsContainer removeSubviews];
    for (NSMutableDictionary *dict in User.incidents){
        RAlertView *rAV = [[RAlertView alloc] init];
        [self.incidentsContainer addSubview:rAV];
        [rAV setupWithTitle:alerts[dict[@"type"]][@"title"] text:alerts[dict[@"type"]][@"text"] img:alerts[dict[@"type"]][@"image"]];
    }
    
    [self.incidentsContainer setupAutosizeBySubviews];
    [self.scrollView setup_autosize];
    
}

-(IBAction)logout:(id)sender{
    [User logoutUser];
    [appDelegate openAuthPage];

}

-(IBAction)delAllResutsAction:(id)sender{
    [self showMessageWithTextInput:@"" msg:@"Введите пароль" title:@"Удаление всех отчетов" btns:@[@"Отменить",@"Удалить"] params:@{@"secured":@YES} result:^(int index, NSString *text){
        if (index == 1){
            [self clearResults];
        }
    }];
}

-(void)clearResults{
    
}

-(IBAction)addIncidentAction:(id)sender{

    if (User.incidents.count>0){
        [self showMessage:@"Вы можете добавить только один инцидент" title:@"Ошибка"];
        return;
    }
    _addIncidentVC = [AddIncident new];
    [self.navigationController pushViewController:_addIncidentVC animated:YES];
    
//    [self showMessage:@"" title:@"Выберите инцидент" btns:@[@"Инфаркт",@"Инсульт",@"Коронарное шунтирование",@"Отмена"] result:^(int result){
//        
//    }];
}

@end
