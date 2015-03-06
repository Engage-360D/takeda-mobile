//
//  DietTest.m
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DietTest.h"
#import "ResultRiskAnal.h"

@interface DietTest (){
    NSMutableArray *test_data;
}

@end

@implementation DietTest
@synthesize testId;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    [self.tableView reloadData];
}

-(void)setupInterface{
    [self.getRecommForDiet setupStandartBordered];
    self.getRecommForDiet.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:15.0];

    self.tLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0f];
    self.tableView.backgroundColor = RGB(243, 243, 243);
    
    [self.tableView.tableHeaderView addSeparatorColor:RGB(186, 189, 191)];
    [self.tableView.tableFooterView addTopSeparatorColor:RGB(186, 189, 191)];

}

-(void)initData{
    
    
    [ServData loadDietQuestions:testId completion:^(NSError *error, id result) {
        if ([error answerOk]){
            NSMutableDictionary *variants = [Global recursiveMutable:[result[@"links"][@"answers"] groupByKey:@"id"]];
            NSMutableArray *questions = [Global recursiveMutable:result[@"data"]];
            
            for (NSMutableDictionary *quest in questions){
                NSMutableArray *answers = quest[@"links"][@"answers"];
                NSMutableArray *new_answers = [NSMutableArray new];
                for (id key in answers){
                    [new_answers addObject:variants[key]];
                }
                [quest setObject:new_answers forKey:@"answers"];
            }
            

            test_data = questions;
            [self.tableView reloadData];

        } else {
            
        }
    }];
    
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return test_data.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSMutableDictionary *item = test_data[indexPath.row];
    float tH = [Global text:item[@"question"] sizeWithFont:[self cellTitleFont] constrainedToSize:CGSizeMake(tableView.width - 30, CGFLOAT_MAX)].height;
    return tH + 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"DietTestTableCell";
    
    DietTestTableCell *cell = (DietTestTableCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"DietTestTableCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[DietTestTableCell class]])
            {
                cell = (DietTestTableCell *)currentObject;
                [cell.segment addTarget:self action:@selector(selectIndex:) forControlEvents:UIControlEventValueChanged];
                break;
            }
        }
    }
        NSMutableDictionary *item = test_data[indexPath.row];

    NSMutableArray*segments = item[@"answers"];
    [cell.segment removeAllSegments];
    for (NSMutableDictionary *answer in segments){
        [cell.segment insertSegmentWithTitle:answer[@"answer"] atIndex:cell.segment.numberOfSegments animated:NO];
    }
    if ([Global isNotNull:item[@"state"]]){
        cell.segment.selectedSegmentIndex = [item[@"state"] intValue]-1;
    } else {
        cell.segment.selectedSegmentIndex = UISegmentedControlNoSegment;
    }
    [cell updateSegmentLabel];

    
    cell.title.text = item[@"question"];
    cell.title.font = [self cellTitleFont];
    cell.title.height = [Global text:item[@"question"] sizeWithFont:[self cellTitleFont] constrainedToSize:CGSizeMake(tableView.width - 30, CGFLOAT_MAX)].height;
    cell.segment.y = cell.title.bottom+10;
    cell.segment.tag = indexPath.row;
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark -

-(void)selectIndex:(UISegmentedControl*)sControl{
    NSMutableDictionary *item = test_data[sControl.tag];
    [item setObject:[NSNumber numberWithInt:sControl.selectedSegmentIndex+1] forKey:@"state"];
}


-(IBAction)getRecommendationAction:(id)sender{
    BOOL allFields = YES;
    NSMutableDictionary *params = [NSMutableDictionary new];
    for (NSMutableDictionary *quest in test_data){
        if ([Global isNotNull:quest[@"state"]]){
            [params setObject:quest[@"state"] forKey:[NSString stringWithFormat:@"answers[%@]",quest[@"id"]]];
        } else {
            allFields = NO;
        }
    }
    if (!allFields){
        [self showMessage:@"Заполните все поля" title:@"Уведомление"];
        return;
    }
    
    [ServData sendToServerDietResultsDiet:testId testData:params completion:^(BOOL success, NSError *error, id result) {
        if (success){
            [GlobalData saveResultDiet:result[@"data"] testId:testId];
            [self goToResult:result[@"data"]];
        }
    }];
}

-(void)goToResult:(NSMutableDictionary*)result{
    
    [(ResultRiskAnal*)self.parentVC goToResult:result];
    
//    _dietTestResults = [DietTestResults new];
//    _dietTestResults.result = result;
//   [self.navigationController pushViewController:_dietTestResults animated:YES];
}

-(UIFont*)cellTitleFont{
    return [UIFont fontWithName:@"Helvetica-Light" size:14];
}

@end
